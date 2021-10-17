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
        List<Diary> diaries = em.createNamedQuery(JpaConst.Q_DIA_GET_ALL, Diary.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return DiaryConverter.toViewList(diaries);
    }
    /**
     * 日記テーブルのデータの件数を取得し、返却する
     * @return データの件数
     */
    public long countAll() {
        long diaries_count = (long) em.createNamedQuery(JpaConst.Q_DIA_COUNT, Long.class)
                .getSingleResult();
        return diaries_count;
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
    public List<String> create(DiaryView rv) {
        List<String> errors = DiaryValidator.validate(rv);
        if (errors.size() == 0) {
            LocalDateTime ldt = LocalDateTime.now();
            rv.setCreatedAt(ldt);
            rv.setUpdatedAt(ldt);
            createInternal(rv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * 画面から入力された日記の登録内容を元に、日記データを更新する
     * @param rv 日記の更新内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> update(DiaryView rv) {

        //バリデーションを行う
        List<String> errors = DiaryValidator.validate(rv);

        if (errors.size() == 0) {

            //更新日時を現在時刻に設定
            LocalDateTime ldt = LocalDateTime.now();
            rv.setUpdatedAt(ldt);

            updateInternal(rv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
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
     * @param rv 日記データ
     */
    private void createInternal(DiaryView rv) {

        em.getTransaction().begin();
        em.persist(DiaryConverter.toModel(rv));
        em.getTransaction().commit();

    }

    /**
     * 日記データを更新する
     * @param rv 日記データ
     */
    private void updateInternal(DiaryView rv) {

        em.getTransaction().begin();
        Diary r = findOneInternal(rv.getId());
        DiaryConverter.copyViewToModel(r, rv);
        em.getTransaction().commit();

    }
}
