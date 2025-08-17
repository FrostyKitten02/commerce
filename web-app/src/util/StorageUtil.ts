

export default class StorageUtil {
    private constructor() {}
    private static SESSION_KEY = "session";
    private static ADMIN_SESSION_KEY = "admin_session";

    public static getSessionToken(): string | null {
        return localStorage.getItem(this.SESSION_KEY);
    }

    public static setSessionToken(token: string): void {
        localStorage.setItem(this.SESSION_KEY, token)
    }

    public static clearSession(): void {
        localStorage.removeItem(this.SESSION_KEY);
        localStorage.removeItem(this.ADMIN_SESSION_KEY);
    }

    public static setAdminSession(isAdmin: boolean): void {
        if (isAdmin) {
            localStorage.setItem(this.ADMIN_SESSION_KEY, "true");
        } else {
            localStorage.removeItem(this.ADMIN_SESSION_KEY);
        }
    }

    public static isAdminSession(): boolean {
        return localStorage.getItem(this.ADMIN_SESSION_KEY) === "true";
    }

    public static isLoggedIn(): boolean {
        return this.getSessionToken() !== null;
    }

    public static logout(): void {
        this.clearSession();
        // Optionally redirect to home page or reload
        window.location.href = '/';
    }

}