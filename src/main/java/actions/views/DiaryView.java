package actions.views;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 日報情報について画面の入力値・出力値を扱うViewモデル
 *
 */
@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
public class DiaryView {
    /**
     * id
     */
    private Integer id;

    /**
     * 日記を書いた人
     */
    private String name;

    /**
     * いつの日報かを示す日付
     */
    private LocalDate diaryDate;

    /**
     * 日報のタイトル
     */
    private String title;

    /**
     * 日報の内容
     */
    private String content;

    /**
     * 登録日時
     */
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    private LocalDateTime updatedAt;
    /**
     * 削除されたかどうか（現役：0、削除済み：1）
     */
    private Integer deleteFlag;
}