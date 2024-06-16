import {createContext, Dispatch, SetStateAction} from "react";
import type { AuthResponse } from '../types/auth/AuthResponse'

interface AuthContextProps {
    authCredential: AuthResponse;
    isAuth: boolean;
    setIsAuth: Dispatch<SetStateAction<boolean>>;
    setAuthCredential: Dispatch<SetStateAction<AuthResponse>>;
}

export const AuthContext = createContext<AuthContextProps>({} as AuthContextProps);
