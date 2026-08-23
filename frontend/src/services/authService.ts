export interface UserResponse {
    id: number;
    username: string;
    email: string;
    createdAt: string;
}

export interface AuthResponse {
    token: string;
    user: UserResponse;
}

export class AuthApiError extends Error {
    fieldErrors?: Record<string, string>;

    constructor(message: string, fieldErrors?: Record<string, string>) {
        super(message);
        this.fieldErrors = fieldErrors;
    }
}

const parseErrorBody = async (response: Response): Promise<AuthApiError> => {
    const body = await response.json().catch(() => null);

    if (body?.error) {
        return new AuthApiError(body.error);
    }

    if (body && typeof body === 'object') {
        // 400 validation responses are a field-name -> message map
        const [firstMessage] = Object.values(body) as string[];
        return new AuthApiError(firstMessage ?? 'Request failed', body as Record<string, string>);
    }

    return new AuthApiError(`Request failed: ${response.status}`);
};

export const authService = {
    async signup(username: string, email: string, password: string): Promise<UserResponse> {
        const response = await fetch('http://localhost:8080/auth/signup', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password })
        });

        if (!response.ok) {
            throw await parseErrorBody(response);
        }

        return await response.json();
    },

    async login(email: string, password: string): Promise<AuthResponse> {
        const response = await fetch('http://localhost:8080/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        if (!response.ok) {
            throw await parseErrorBody(response);
        }

        return await response.json();
    },

    async me(token: string): Promise<UserResponse> {
        const response = await fetch('http://localhost:8080/auth/me', {
            headers: { Authorization: `Bearer ${token}` }
        });

        if (!response.ok) {
            throw await parseErrorBody(response);
        }

        return await response.json();
    }
};
