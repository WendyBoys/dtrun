import {request} from '../../request'

export async function login(params) {
    return await request("/user/login", "post", params)
}

export async function register(params) {
    return await request("/user/register", "post", params)
}

export async function getCurrentUser(params) {
    return await request("/user/getCurrentUser", "get", params)
}

export async function modifyPassword(params) {
    return await request("/user/modifyPassword", "post", params)
}

