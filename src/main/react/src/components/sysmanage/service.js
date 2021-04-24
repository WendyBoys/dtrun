import {request} from '../../request'

export async function getLogsById(params) {
    return await request("/sysmanage/getLogsById", "get", params)
}
export async function getColorById(params) {
        return await request("/sysmanage/getColorById", "post", params)
}
export async function findAll(params) {
    return await request("/sysmanage/findAll", "get", params)
}
export async function createWhiteList(params) {
    return await request("/sysmanage/createWhiteList", "post", params)
}
export async function getWhiteListById(params) {
    return await request("/sysmanage/getWhiteListById", "post", params)
}
export async function updateWhiteList(params) {
    return await request("/sysmanage/updateWhiteList", "post", params)
}
export async function deletes(params) {
    return await request("/sysmanage/deleteWhiteList", "post", params)
}