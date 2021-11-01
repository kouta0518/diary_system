package models;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 日記データのDTOモデル
 *
 */
@Table(name = JpaConst.TABLE_DIA)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_DIA_GET_ALL,
            query = JpaConst.Q_DIA_GET_ALL_DEF),
   // @NamedQuery(
     //       name = JpaConst.Q_DIA_COUNT,
      //      query = JpaConst.Q_DIA_COUNT_DEF),
    @NamedQuery(
            name = JpaConst.Q_DIA_DEL_FALSE,
            query = JpaConst.Q_DIA_DEL_FALSE_DEF),
})

@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class Diary {
    /**
     * id
     */
    @Id
    @Column(name = JpaConst.DIA_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 書いた人の名前
     */
    @Column(name = JpaConst.DIA_COL_NAME)
    private String name;

    /**
     * いつの日報かを示す日付
     */
    @Column(name = JpaConst.DIA_COL_DIA_DATE, nullable = false)
    private LocalDate diaryDate;

    /**
     * 日報のタイトル
     */
    @Column(name = JpaConst.DIA_COL_TITLE, length = 255, nullable = false)
    private String title;

    /**
     * 日報の内容
     */
    @Lob
    @Column(name = JpaConst.DIA_COL_CONTENT, nullable = false)
    private String content;

    /**
     * 登録日時
     */
    @Column(name = JpaConst.DIA_COL_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    @Column(name = JpaConst.DIA_COL_UPDATED_AT, nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 削除されたかどうか（現役：0、削除済み：1）
     */
    @Column(name = JpaConst.DIA_COL_DELETE_FLAG, nullable = false)
    private Integer deleteFlag;
}
