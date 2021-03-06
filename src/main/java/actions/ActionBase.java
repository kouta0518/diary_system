package actions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import constants.AttributeConst;
import constants.ForwardConst;

public abstract class ActionBase {
    protected ServletContext context;
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    /**
     * 初期化処理
     * サーブレットコンテキスト、リクエスト、レスポンスをクラスフィールドに設定
     * 各クラスフィールドの値を設定します。
     * @param servletContext
     * @param servletRequest
     * @param servletResponse
     */
    public void init(     //doGetから飛んできた３つのインスタスがActionBaseで使えますよということ
            ServletContext servletContext,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {
        this.context = servletContext;
        this.request = servletRequest;
        this.response = servletResponse;
    }

    /**
     * フロントコントローラから直接呼び出されるメソッドです。各サブクラスで内容を実装します。
     * @throws ServletException
     * @throws IOException
     */
    public abstract void process() throws ServletException, IOException;

    /**
     * パラメータのcommandの値に該当するメソッドを実行します。commandの値が不正の場合はエラー画面を呼び出します。
     * @throws ServletException
     * @throws IOException
     */
    protected void invoke()
            throws ServletException, IOException {

        Method commandMethod;
        try {

            //パラメータからcommandを取得
            String command = request.getParameter(ForwardConst.CMD.getValue());

            /**
            * commandに該当するメソッドを実行する
            *(例: action=Diary command=show の場合 DiaryActionクラスのshow()メソッドを実行する)
            *このメソッドの第一引数に与える文字列がメソッドの名前です。第二引数はメソッドの引数です。引数がない場合は new Class[0] と記述します。
            *this.getClass().getDeclaredMethod("index", new Class[0]);と記述すると、thisが表すクラスの index() メソッド、という意味
            */              //this.getClass()はDiaryActionクラスのこと
            commandMethod = this.getClass().getDeclaredMethod(command, new Class[0]);
            commandMethod.invoke(this, new Object[0]); //メソッドに渡す引数はなし

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NullPointerException e) {

            //発生した例外をコンソールに表示
            e.printStackTrace();
            //commandの値が不正で実行できない場合エラー画面を呼び出し
            forward(ForwardConst.FW_ERR_UNKNOWN);
        }

    }

    /**
     * 指定されたjspの呼び出しを行う
     * @param target 遷移先jsp画面のファイル名(拡張子を含まない)
     * @throws ServletException
     * @throws IOException
     */
    protected void forward(ForwardConst target) throws ServletException, IOException {

        //jspファイルの相対パスを作成、jspファイルの存在する場所を示す文字列
        //%s とは文字列として埋め込めという指定子です
        //targetがForwardConst.FW_DIA_INDEXになる、DiaryActionのindexメソットの最後に書いてあるforwardメソットから
        String forward = String.format("/WEB-INF/views/%s.jsp", target.getValue());//%s = diaries/index
        RequestDispatcher dispatcher = request.getRequestDispatcher(forward);

        //jspファイルの呼び出し
        dispatcher.forward(request, response);//WEB-INFフォルダの中にあるviewsフォルダの中にあるdiariesフォルダの中にあるindex.jspファイルを表示しなさいということです

    }

    /**
     * URLを構築しリダイレクトを行う
     * @param action パラメータに設定する値
     * @param command パラメータに設定する値
     * @throws ServletException
     * @throws IOException
     */
    protected void redirect(ForwardConst action, ForwardConst command)
            throws ServletException, IOException {

        //URLを構築
        String redirectUrl = request.getContextPath() + "/?action=" + action.getValue();
        if (command != null) {
            redirectUrl = redirectUrl + "&command=" + command.getValue();
        }

        //URLへリダイレクト
        response.sendRedirect(redirectUrl);

    }

    /**
     * CSRF対策 token不正の場合はエラー画面を表示
     * @return true: token有効 false: token不正
     * @throws ServletException
     * @throws IOException
     */
    protected boolean checkToken() throws ServletException, IOException {

        //パラメータからtokenの値を取得、トークンとは、パラメータの参照IDで、レポートに対してURLを生成する際に使用されます
        String _token = getRequestParam(AttributeConst.TOKEN);

        if (_token == null || !(_token.equals(getTokenId()))) {

            //tokenが設定されていない、またはセッションIDと一致しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

            return false;
        } else {
            return true;
        }

    }

    /**
     * セッションIDを取得する
     * セッションIDとは、Webアプリケーションなどで、通信中の利用者を識別して行動を捕捉し、利用者ごとに一貫したサービスを提供するために付与される固有の識別情報。
     * @return セッションID
     */
    protected String getTokenId() {
        return request.getSession().getId();
    }

    /**
     * リクエストから表示を要求されているページ数を取得し、返却する
     * 一覧表示のページネーションで利用します。
     * ページネイションとは、検索結果一覧やカテゴリ一覧などのリスト化された縦長ページを複数ページに分割する機能のこと
     * @return 要求されているページ数(要求がない場合は1)
     */
    protected int getPage() {
        int page;
        page = toNumber(request.getParameter(AttributeConst.PAGE.getValue()));
        if (page == Integer.MIN_VALUE) {
            page = 1;
        }
        return page;
    }

    /**
     * 文字列を数値に変換する
     * @param strNumber 変換前文字列
     * @return 変換後数値
     */
    protected int toNumber(String strNumber) {
        int number = 0;
        try {
            number = Integer.parseInt(strNumber);//parseIntは引数に指定した文字列を int 型の値に変換し戻り値として返します
        } catch (Exception e) {
            number = Integer.MIN_VALUE;//1という数字がもらえる
        }
        return number;
    }

    /**
     * 文字列をLocalDate型に変換する
     * @param strDate 変換前文字列
     * @return 変換後LocalDateインスタンス
     */
    protected LocalDate toLocalDate(String strDate) {
        if (strDate == null || strDate.equals("")) {
            return LocalDate.now();
        }
        return LocalDate.parse(strDate);
    }

    /**
     * リクエストスコープから指定されたパラメータの値を取得し、返却する
     * @param key パラメータ名
     * @return パラメータの値
     */
    protected String getRequestParam(AttributeConst key) {
        return request.getParameter(key.getValue());
    }

    /**
     * リクエストスコープにパラメータを設定する、リクエストスコープとはリクエスト内でのみ使用できる情報、もしくはその「範囲」。
     * 第2引数の型Vはジェネリクス（Generics・総称型）というものです。与えた引数の型がこのVにあたる、という意味です。すべての型を引数にとることができます。
     * @param key パラメータ名
     * @param value パラメータの値
     */ //Genericsとは、クラス、インターフェース、メソッドなどの「型」を「パラメータとして定義する」ことを可能にしたものです
    protected <V> void putRequestScope(AttributeConst key, V value) {
        request.setAttribute(key.getValue(), value);
    }

    /**
     * セッションスコープから指定されたパラメータの値を取得し、返却する
     * リクエスト後も情報を残したい場合にはセッションに情報を保存する。この情報のスコープは「セッションスコープ」となる。
     * @param key パラメータ名
     * @return パラメータの値
     */  //セッションにはあらゆる型のオブジェクトを格納できます
    @SuppressWarnings("unchecked")
    protected <R> R getSessionScope(AttributeConst key) {
        return (R) request.getSession().getAttribute(key.getValue());
    }

    /**
     * セッションスコープにパラメータを設定する
     * @param key パラメータ名
     * @param value パラメータの値
     */
    protected <V> void putSessionScope(AttributeConst key, V value) {
        request.getSession().setAttribute(key.getValue(), value);
    }

    /**
     * セッションスコープから指定された名前のパラメータを除去する
     * @param key パラメータ名
     */
    protected void removeSessionScope(AttributeConst key) {
        request.getSession().removeAttribute(key.getValue());
    }
}

