package actions;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.DiaryView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.DiaryService;
/**
 * 日報に関する処理を行うActionクラス
 *
 */
public class DiaryAction extends ActionBase {
    private DiaryService service;
    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {
        service = new DiaryService();

      //メソッドを実行
        invoke();
        service.close();
    }
    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {
        //指定されたページ数の一覧画面に表示するデータを取得
        int page = getPage();
        List<DiaryView> diaries = service.getAllPerPage(page);
        //全日記データを取得
        long diariesCount = service.countAll();

        putRequestScope(AttributeConst.DIARIES, diaries);//取得した日記データ
        putRequestScope(AttributeConst.DIA_COUNT, diariesCount);//全ての日記データの件数
        putRequestScope(AttributeConst.PAGE, page);//ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE);// 一ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }
        //
        forward(ForwardConst.FW_DIA_INDEX);
    }
}
