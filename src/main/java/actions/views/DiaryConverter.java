package actions.views;
import java.util.ArrayList;
import java.util.List;

import models.Diary;
/**
 * 日記データのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class DiaryConverter {
    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param rv DiaryViewのインスタンス
     * @return Diaryのインスタンス
     */
    public static Diary toModel(DiaryView rv) {
        return new Diary(
                rv.getId(),
                rv.getName(),
                rv.getDiaryDate(),
                rv.getTitle(),
                rv.getContent(),
                rv.getCreatedAt(),
                rv.getUpdatedAt());
    }
    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param r Diaryのインスタンス
     * @return DiaryViewのインスタンス
     */
    public static DiaryView toView(Diary r) {
        if(r == null) {
            return null;
        }

        return new DiaryView(
                r.getId(),
                r.getName(),
                r.getDiaryDate(),
                r.getTitle(),
                r.getContent(),
                r.getCreatedAt(),
                r.getUpdatedAt());
    }
    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<DiaryView> toViewList(List<Diary> list) {
        List<DiaryView> evs = new ArrayList<>();

        for (Diary r : list) {
            evs.add(toView(r));
        }
        return evs;
    }
    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param r DTOモデル(コピー先)
     * @param rv Viewモデル(コピー元)
     */
    public static void copyViewToModel(Diary r, DiaryView rv) {
        r.setId(rv.getId());
        r.setName(rv.getName());
        r.setDiaryDate(rv.getDiaryDate());
        r.setTitle(rv.getTitle());
        r.setContent(rv.getContent());
        r.setCreatedAt(rv.getCreatedAt());
        r.setUpdatedAt(rv.getUpdatedAt());
}
    /**
     * DTOモデルの全フィールドの内容をViewモデルのフィールドにコピーする
     * @param r DTOモデル(コピー元)
     * @param rv Viewモデル(コピー先)
     */
    public static void copyModelToView(Diary r, DiaryView rv) {
        rv.setId(r.getId());
        rv.setName(r.getName());
        rv.setDiaryDate(r.getDiaryDate());
        rv.setTitle(r.getTitle());
        rv.setCreatedAt(r.getCreatedAt());
        rv.setUpdatedAt(r.getUpdatedAt());
    }
}
