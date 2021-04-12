import {request} from '../../request'

export async function findAll(params) {
    return await request("/contact/findAll", "get", params)
}
export async function deletes(params) {
    return await request("/contact/delete", "post", params)
}
export async function createContact(params) {
    return await request("/contact/createContact", "post", params)
}
export async function getContactById(params) {
    return await request("/contact/getContactById", "get", params)
}
export async function updateContact(params) {
    return await request("/contact/updateContact", "post", params)
}

export async function getContactLists(params) {
    return await request("/contact/findAll", "get", params)
}
