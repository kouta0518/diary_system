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
     * @param dv DiaryViewのインスタンス
     * @return Diaryのインスタンス
     */
    public static Diary toModel(DiaryView dv) {
        return new Diary(
                dv.getId(),
                dv.getName(),
                dv.getDiaryDate(),
                dv.getTitle(),
                dv.getContent(),
                dv.getCreatedAt(),
                dv.getUpdatedAt(),
                dv.getDeleteFlag());

    }
    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param d Diaryのインスタンス
     * @return DiaryViewのインスタンス
     */
    public static DiaryView toView(Diary d) {
        if(d == null) {
            return null;
        }

        return new DiaryView(
                d.getId(),
                d.getName(),
                d.getDiaryDate(),
                d.getTitle(),
                d.getContent(),
                d.getCreatedAt(),
                d.getUpdatedAt(),
                d.getDeleteFlag());
    }
    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<DiaryView> toViewList(List<Diary> list) {
        List<DiaryView> evs = new ArrayList<>();

        for (Diary d : list) {
            evs.add(toView(d));
        }
        return evs;
    }
    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param d DTOモデル(コピー先)
     * @param dv Viewモデル(コピー元)
     */
    public static void copyViewToModel(Diary d, DiaryView dv) {
        d.setId(dv.getId());
        d.setName(dv.getName());
        d.setDiaryDate(dv.getDiaryDate());
        d.setTitle(dv.getTitle());
        d.setContent(dv.getContent());
        d.setCreatedAt(dv.getCreatedAt());
        d.setUpdatedAt(dv.getUpdatedAt());
}
    /**
     * DTOモデルの全フィールドの内容をViewモデルのフィールドにコピーする
     * @param d DTOモデル(コピー元)
     * @param dv Viewモデル(コピー先)
     */
    public static void copyModelToView(Diary d, DiaryView dv) {
        dv.setId(d.getId());
        dv.setName(d.getName());
        dv.setDiaryDate(d.getDiaryDate());
        dv.setTitle(d.getTitle());
        dv.setCreatedAt(d.getCreatedAt());
        dv.setUpdatedAt(d.getUpdatedAt());
    }
}
