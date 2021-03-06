package constants;
//画面の項目値等を定義する
//Enumクラス、これは列挙型で定数をまとめて管理できるということ
public enum AttributeConst {
  //フラッシュメッセージ
    FLUSH("flush"),

    //一覧画面共通
    MAX_ROW("maxRow"),
    PAGE("page"),

    //入力フォーム共通
    TOKEN("_token"),
    ERR("errors"),


    //ログイン画面
    LOGIN_ERR("loginError"),

    //削除フラグ
    DEL_FLAG_TRUE(1),
    DEL_FLAG_FALSE(0),

    //日記管理
    DIARY("diary"),
    DIARIES("diaries"),
    DIA_ID("id"),
    DIA_DATE("diary_date"),
    DIA_NAME("name"),
    DIA_TITLE("title"),
    DIA_CONTENT("content"),
    DIA_DESTROY("destroy");

    private final String text;
    private final Integer i;

    private AttributeConst(final String text) {
        this.text = text;
        this.i = null;
    }

    private AttributeConst(final Integer i) {
        this.text = null;
        this.i = i;
    }

    public String getValue() {
        return this.text;
    }

    public Integer getIntegerValue() {
        return this.i;
    }

}

