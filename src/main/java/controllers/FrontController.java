package controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import actions.ActionBase;
import actions.UnknownAction;
import constants.ForwardConst;

/**
 * フロントコントローラ
 *
 */
@WebServlet("/")
public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public FrontController() {
        super();
        /**
        * あるクラスをインスタンス化する際には、何もしなくとも親クラスのコンストラクタが勝手に呼ばれるようになっているわけですが、勝手に呼ばせるのではなく、自分でコーディングすることで親クラスのコンストラクタを呼ぶことも出来ます。それがsuper( )です
        * コンストラクタの一行目です。限定です。それ以外の場所にsuper( )を書くとコンパイルエラーになります。
        */
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //パラメータに該当するActionクラスのインスタンス
        ActionBase action = getAction(request, response);

        //サーブレットコンテキスト、リクエスト、レスポンスをActionインスタンスのフィールドに設定
        action.init(getServletContext(), request, response);

        //Actionクラスの処理を呼び出し
        action.process();
    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * リクエストパラメータの値から該当するActionクラスのインスタンスを作成し、返却する
     * (例:パラメータが action=Diary の場合、actions.DiaryActionオブジェクト)
     * @param request リクエスト
     * @param response レスポンス
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" }) //コンパイラ警告を抑制
    private ActionBase getAction(HttpServletRequest request, HttpServletResponse response) {
        Class type = null;
        ActionBase action = null;
        try {

            //リクエストからパラメータ"action"の値を取得
            String actionString = request.getParameter(ForwardConst.ACT.getValue());

            //該当するActionオブジェクトを作成 (例:リクエストからパラメータ action=Diary の場合、actions.DiaryActionオブジェクト)
            // "actions.DiaryAction"という文字列から actionsパッケージにある DiaryActionというクラス型を取得していることに他なりません。
            //forNameは、クラス名を元にそのクラスのインスタンスを作成するメソッドです
            type = Class.forName(String.format("actions.%sAction", actionString));//%s とは文字列として埋め込めという指定子です
/**
 * http://localhost:8080/diary_system/?action=Diary&command=index
というURLにアクセスすると、クエリパラメータにより actionというキーワードに紐づいて Diaryという値が飛んできてそれを取得しています。クエリーパラメータはURLに付け加える情報。
 */
            //ActionBaseのオブジェクトにキャスト(例:actions.DiaryActionオブジェクト→actions.ActionBaseオブジェクト)
            action = (ActionBase) (type.asSubclass(ActionBase.class)
                    .getDeclaredConstructor()//その型に対応したクラスのインスタンスを生成している箇所です。リフレクションという。
                    .newInstance());

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SecurityException
                | IllegalArgumentException | InvocationTargetException| NoSuchMethodException e) {

            //リクエストパラメータに設定されている"action"の値が不正の場合(例:action=xxxxx 等、該当するActionクラスがない場合)
            //エラー処理を行うActionオブジェクトを作成
            action = new UnknownAction();
        }
        return action;//完成したDiaryActionのインスタンスをあげると言っている
    }

}