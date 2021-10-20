package models.validators;
import java.util.ArrayList;
import java.util.List;

import actions.views.DiaryView;
import constants.MessageConst;

/**
 * 日記インスタンスに設定されている値のバリデーションを行うクラス
 */
public class DiaryValidator {
    /**
     * 日記インスタンスの各項目についてバリデーションを行う
     * @param rv 日記インスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(DiaryView rv) {
        List<String> errors = new ArrayList<String>();
        //名前のチェック
        String nameError = validateName(rv.getName());
        if (!nameError.equals("")) {
            errors.add(nameError);
        }

        //タイトルチェック
        String titleError = validateTitle(rv.getTitle());
        if (!titleError.equals("")) {
            errors.add(titleError);
        }

        //内容のチェック
        String contentError = validateContent(rv.getContent());
        if (!contentError.equals("")) {
            errors.add(contentError);
        }

        return errors;
    }
    /**
     * 名前に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param name タイトル
     * @return エラーメッセージ
     */
    private static String validateName(String name) {
        if (name == null || name.equals("")) {
            return MessageConst.E_NONAME.getMessage();
        }
        //入力がある場合は空文字を返却
        return "";
    }


    /**
     * タイトルに入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param title タイトル
     * @return エラーメッセージ
     */
    private static String validateTitle(String title) {
        if (title == null || title.equals("")) {
            return MessageConst.E_NOTITLE.getMessage();
        }
      //入力値がある場合は空文字を返却
        return "";
    }
    /**
     * 内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param content 内容
     * @return エラーメッセージ
     */
    private static String validateContent(String content) {
        if (content == null || content.equals("")) {
            return MessageConst.E_NOCONTENT.getMessage();
        }
        //入力値がある場合は空文字を返却
        return "";
    }
}
