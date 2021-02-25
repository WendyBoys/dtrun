import {request} from '../../request'

export async function getLogsById(params) {
    return await request("/sysmanage/getLogsById", "get", params)
}
