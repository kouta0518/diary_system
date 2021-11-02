package constants;

/**
 * DB関連の項目値を定義するインターフェース
 * ※インターフェイスに定義した変数は public static final 修飾子がついているとみなされる、明示的な修飾子の記述は不要です。
 */
public interface JpaConst {

    //persistence-unit名
    String PERSISTENCE_UNIT_NAME = "diary_system";

    //データ取得件数の最大値
    int ROW_PER_PAGE = 15; //1ページに表示するレコードの数

    int DIA_DEL_TRUE = 1; //削除フラグON(削除済み)
    int DIA_DEL_FALSE = 0; //削除フラグOFF(現役)

    //日報テーブル
    String TABLE_DIA = "diaries"; //テーブル名
    //日報テーブルカラム
    String DIA_COL_ID = "id"; //id
    String DIA_COL_NAME = "name"; //書いた人の名前
    String DIA_COL_DIA_DATE = "diary_date"; //いつの日記かを示す日付
    String DIA_COL_TITLE = "title"; //日記のタイトル
    String DIA_COL_CONTENT = "content"; //日記の内容
    String DIA_COL_CREATED_AT = "created_at"; //登録日時
    String DIA_COL_UPDATED_AT = "updated_at"; //更新日時
    String DIA_COL_DELETE_FLAG = "delete_flag"; //削除フラグ

    //Entity名
    String ENTITY_DIA = "diary"; //日記

    //NamedQueryの nameとquery

    //全ての日報をidの降順に取得する
    String Q_DIA_GET_ALL = ENTITY_DIA + ".getAll";
    String Q_DIA_GET_ALL_DEF = "SELECT r FROM Diary AS r ORDER BY r.id DESC";
    //SQLがdelete_flag=0のものだけを取って来るようにする
    String Q_DIA_DEL_FALSE = ENTITY_DIA + ".getAllNotDelete";
    String Q_DIA_DEL_FALSE_DEF = "SELECT d FROM Diary AS d WHERE d.deleteFlag=0 ORDER BY d.id DESC";

}

