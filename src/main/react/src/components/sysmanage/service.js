import {request} from '../../request'

export async function getLogsById(params) {
    return await request("/sysmanage/getLogsById", "get", params)
}
export async function getColorById(params) {
        return await request("/sysmanage/getColorById", "post", params)
}
