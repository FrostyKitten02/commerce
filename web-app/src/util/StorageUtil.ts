

export default class StorageUtil {
    private constructor() {}
    private static SESSION_KEY = "session";

    public static getSessionToken(): string | null {
        return localStorage.getItem(this.SESSION_KEY);
    }

    public static setSessionToken(token: string): void {
        localStorage.setItem(this.SESSION_KEY, token)
    }

}