package constants;

/**
 * DB関連の項目値を定義するインターフェース
 * ※インターフェイスに定義した変数は public static final 修飾子がついているとみなされる
 */
public interface JpaConst {

    //persistence-unit名
    String PERSISTENCE_UNIT_NAME = "diary_system";

    //データ取得件数の最大値
    int ROW_DIA_PAGE = 15; //1ページに表示するレコードの数

    int ROLE_ADMIN = 1; //管理者権限ON(管理者)
    int ROLE_GENERAL = 0; //管理者権限OFF(一般)

    //日報テーブル
    String TABLE_DIA = "diaries"; //テーブル名
    //日報テーブルカラム
    String DIA_COL_ID = "id"; //id
    String DIA_COL_REP_DATE = "diary_date"; //いつの日記かを示す日付
    String DIA_COL_TITLE = "title"; //日記のタイトル
    String DIA_COL_CONTENT = "content"; //日記の内容
    String DIA_COL_CREATED_AT = "created_at"; //登録日時
    String DIA_COL_UPDATED_AT = "updated_at"; //更新日時

    //Entity名
    String ENTITY_DIA = "diary"; //日記

    //JPQL内パラメータ
    String JPQL_PARM_CODE = "code"; //社員番号
    String JPQL_PARM_PASSWORD = "password"; //パスワード

    //NamedQueryの nameとquery

    //全ての日報をidの降順に取得する
    String Q_DIA_GET_ALL = ENTITY_DIA + ".getAll";
    String Q_DIA_GET_ALL_DEF = "SELECT r FROM Diary AS r ORDER BY r.id DESC";
    //全ての日報の件数を取得する
    String Q_DIA_COUNT = ENTITY_DIA + ".count";
    String Q_DIA_COUNT_DEF = "SELECT COUNT(r) FROM Diary AS r";
    //指定した従業員が作成した日報を全件idの降順で取得する
    String Q_DIA_GET_ALL_MINE = ENTITY_DIA + ".getAllMine";
    //指定した従業員が作成した日報の件数を取得する
    String Q_DIA_COUNT_ALL_MINE = ENTITY_DIA + ".countAllMine";

}

