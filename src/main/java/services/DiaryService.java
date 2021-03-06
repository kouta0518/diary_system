package services;
import java.time.LocalDateTime;
import java.util.List;

import actions.views.DiaryConverter;
import actions.views.DiaryView;
import constants.JpaConst;
import models.Diary;
import models.validators.DiaryValidator;

/**
 * 日記テーブルの操作に関わる処理を行うクラス
 */
public class DiaryService extends ServiceBase {
    /**
     * 指定されたページ数の一覧画面に表示する日記データを取得し、DairyViewのリストで返却する
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<DiaryView> getAllPerPage(int page) {
        List<Diary> diaries = em.createNamedQuery(JpaConst.Q_DIA_DEL_FALSE, Diary.class)//em = diary_system, EntityManagerクラスの「createNamedQuery」メソッドを使います。引数には、利用するNamedQueryの名前（nameで指定した値）を用意してやります。これにより、そのNamedQueryに用意されたqueryのクエリーを実行するためのQueryインスタンスが作成されます。
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))//動的ファインダーメソッドの整数引数にこれで注釈を付けて、関心のある結果セットの最初の結果のインデックスを渡します。結果セットのページングに役立ちます。よって補完され MaxResultsます
                .setMaxResults(JpaConst.ROW_PER_PAGE)//動的ファインダーメソッドの整数引数にこれで注釈を付けて、返される結果ウィンドウの最大サイズを渡します。結果セットのページングに役立ちます。の補集合FirstResult。
                .getResultList();//このオブジェクトは、機能の結果を戻すために、拡張ユーザ定義機能で使用されます。
        return DiaryConverter.toViewList(diaries);
    }

    /**
     * idを条件に取得したデータをDiaryViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public DiaryView findOne(int id) {
        return DiaryConverter.toView(findOneInternal(id));
    }

    /**
     * 画面から入力された日記の登録内容を元にデータを1件作成し、日記テーブルに登録する
     * @param rv 日記の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(DiaryView dv) {
        List<String> errors = DiaryValidator.validate(dv);
        if (errors.size() == 0) {          //sizeメソッドを使うことでListの要素数を調べることができます！
            LocalDateTime ldt = LocalDateTime.now();
            dv.setCreatedAt(ldt);
            dv.setUpdatedAt(ldt);
            createInternal(dv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * 画面から入力された日記の登録内容を元に、日記データを更新する
     * @param dv 日記の更新内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> update(DiaryView dv) {

        //バリデーションを行う
        List<String> errors = DiaryValidator.validate(dv);

        if (errors.size() == 0) {

            //更新日時を現在時刻に設定
            LocalDateTime ldt = LocalDateTime.now();
            dv.setUpdatedAt(ldt);

            updateInternal(dv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * idを条件を論理削除する
     * @param id
     */
    public void destroy(Integer id) {

        //idを条件に登録済み取得する
        DiaryView savedDia = findOne(id);

        //論理削除フラグをたてる
        savedDia.setDeleteFlag(JpaConst.DIA_DEL_TRUE);

        //更新処理を行う
        update(savedDia);
    }

    /**
     * idを条件にデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private Diary findOneInternal(int id) {
        return em.find(Diary.class, id);
    }

    /**
     * 日記データを1件登録する
     * @param dv 日記データ
     */
    private void createInternal(DiaryView dv) {

        em.getTransaction().begin();
        em.persist(DiaryConverter.toModel(dv));
        em.getTransaction().commit();
    }

    /**
     * 日記データを更新する
     * @param dv 日記データ
     */
    private void updateInternal(DiaryView dv) {

        em.getTransaction().begin();
        Diary d = findOneInternal(dv.getId());
        DiaryConverter.copyViewToModel(d, dv);
        em.getTransaction().commit();
    }
}
