



export class StringUtil {
    private constructor() {}

    public static emptyOrNull(value: string | null | undefined) {
        if (value == null) {
            return true;
        }

        if (value.trim() == "") {
            return true;
        }

        return false;
    }
}