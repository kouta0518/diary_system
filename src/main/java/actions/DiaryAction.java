package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.DiaryView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.DiaryService;

/**
 * 日記に関する処理を行うActionクラス
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

    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException {
        putRequestScope(AttributeConst.TOKEN, getTokenId());

        DiaryView dv = new DiaryView();
        dv.setDiaryDate(LocalDate.now());
        putRequestScope(AttributeConst.DIARY, dv);

        forward(ForwardConst.FW_DIA_NEW);
    }

    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {
        //CSRF対策 tokenのチェック
        if (checkToken()) {
            //日記の日付が入力されていなければ、今日の日付を設定
            LocalDate day = null;
            if (getRequestParam(AttributeConst.DIA_DATE) == null
                    || getRequestParam(AttributeConst.DIA_DATE).equals("")) {
                day = LocalDate.now();
            } else {
                day = LocalDate.parse(getRequestParam(AttributeConst.DIA_DATE));
            }

            //パラメータの値をもとに日記情報のインスタンスを作成する
            DiaryView dv = new DiaryView(
                    null,
                    getRequestParam(AttributeConst.DIA_NAME),
                    day,
                    getRequestParam(AttributeConst.DIA_TITLE),
                    getRequestParam(AttributeConst.DIA_CONTENT),
                    null,
                    null,
                    null);
            //日記情報登録
            List<String> errors = service.create(dv);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合

                putRequestScope(AttributeConst.TOKEN, getTokenId());//CSRF対策用トークン
                putRequestScope(AttributeConst.DIARY, dv);//入力された日記情報
                putRequestScope(AttributeConst.ERR, errors);//エラーのリスト

                //新規登録画面を再表示
                forward(ForwardConst.FW_DIA_NEW);

            } else {

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_DIA, ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

        //idを条件に日記データを取得する
        DiaryView dv = service.findOne(toNumber(getRequestParam(AttributeConst.DIA_ID)));

        if (dv == null) {
            //該当の日記データが存在しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {
            putRequestScope(AttributeConst.DIARY, dv);//取得した日記データ
          //詳細画面を表示
            forward(ForwardConst.FW_DIA_SHOW);
        }
    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {
        //idを条件に日報データを取得する
        DiaryView dv = service.findOne(toNumber(getRequestParam(AttributeConst.DIA_ID)));

        if (dv == null) {
            //該当の日報データが存在しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst.DIARY, dv); //取得した日報データ

            //編集画面を表示
            forward(ForwardConst.FW_DIA_EDIT);
        }
    }

    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {
        //CSRF対策 tokenのチェック
        if (checkToken()) {
            //idを条件に日記データを取得する
            DiaryView dv = service.findOne(toNumber(getRequestParam(AttributeConst.DIA_ID)));
            //入力された日記内容を設定する
            dv.setName(getRequestParam(AttributeConst.DIA_NAME));
            dv.setDiaryDate(toLocalDate(getRequestParam(AttributeConst.DIA_DATE)));
            dv.setTitle(getRequestParam(AttributeConst.DIA_TITLE));
            dv.setContent(getRequestParam(AttributeConst.DIA_CONTENT));

            //日記データを更新する
            List<String> errors = service.update(dv);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合
                putRequestScope(AttributeConst.TOKEN, getTokenId());//CSRF対策用トークン
                putRequestScope(AttributeConst.DIARY, dv);//入力された日記情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト
                //編集画面を再表示
                forward(ForwardConst.FW_DIA_EDIT);
            } else {
                //更新中にエラーがなかった場合
                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_DIA, ForwardConst.CMD_INDEX);
            }
        }
    }
    /**
     * 論理削除を行う
     * @throws ServletException
     * @throws IOException
     */
    public void destroy() throws ServletException, IOException {
        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //idを条件に従業員データを論理削除する
            service.destroy(toNumber(getRequestParam(AttributeConst.DIA_ID)));

            //セッションに削除完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_DELETED.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst.ACT_DIA, ForwardConst.CMD_INDEX);
        }
    }
}
