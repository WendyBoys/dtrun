import {request} from '../../request'

export async function findAll(params) {
    return await request("/dtsource/findAll", "get", params)
}

export async function connection(params) {
    return await request("/dtsource/connection", "post", params)
}

export async function deletes(params) {
    return await request("/dtsource/delete", "delete", params)
}

export async function testconnection(params) {
    return await request("/dtsource/testconnection", "post", params)
}

export async function createDataSource(params) {
    return await request("/dtsource/create", "post", params)
}

export async function getDtSourceById(params) {
    return await request("/dtsource/getDtSourceById", "get", params)
}

export async function updateDataSource(params) {
    return await request("/dtsource/update", "post", params)
}

export async function getAllDtSourceName(params) {
    return await request("/dtsource/getAllDtSourceName", "get", params)
}

export async function getBucketLists(params) {
    return await request("/dtsource/getBucketLists", "get", params)
}

export async function createBucket(params) {
    return await request("/dtsource/createBucket", "post", params)
}



