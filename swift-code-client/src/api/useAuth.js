import React from 'react';
import axios from 'axios';

export function useAuth() {
    const authenticate = async (admin = false, secret = "") => {
        const scope = admin ? ["USER", "ADMIN"] : ["USER"];
        const sub = admin ? "admin" : "user";

        const credentials = {
            sub: sub,
            key: secret,
            scope: scope
        };

        try {
            const response = await axios.post("http://localhost:8080/auth/token", credentials);
            return response.data;
        }
        catch (error) {
            console.error(error);
            return "";
        }
    };

    return authenticate;
}