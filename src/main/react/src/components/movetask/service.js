import {request} from '../../request'

export async function findAll(params) {
    return await request("/movetask/findAll", "get", params)
}

export async function deletes(params) {
    return await request("/movetask/delete", "delete", params)
}

export async function run(params) {
    return await request("/movetask/run", "post", params)
}

export async function quit(params) {
    return await request("/movetask/quit", "post", params)
}

export async function createMoveTask(params) {
    return await request("/movetask/create", "post", params)
}

export async function getMoveTaskById(params) {
    return await request("/movetask/getMoveTaskById", "get", params)
}

export async function updateMoveTask(params) {
    return await request("/movetask/update", "post", params)
}

export async function getResultById(params) {
    return await request("/movetask/getResultById", "get", params)
}
