import axios from 'axios'
import cookie from 'react-cookies'

const HTTP = axios.create({
    baseURL: "http://127.0.0.1:8080",
    headers: {'token': cookie.load('token')}
});

export function request(url, method, param) {
    return new Promise((resolve, reject) => {
        if (method === 'post') {
            HTTP.post(url, param, {
                headers: {'token': cookie.load('token')}
            })
                .then(response => {
                    resolve(response);
                }, err => {
                    reject(err);
                })
                .catch((error) => {
                    reject(error)
                })
        } else if (method === 'get') {
            let params = '?';
            if (param) {
                for (const key in param) {
                    params += key + "=" + param[key] + "&";
                }
            }
            HTTP.get(url + params.substring(0, params.length - 1), {
                headers: {'token': cookie.load('token')}
            })
                .then(response => {
                    resolve(response);
                }, err => {
                    reject(err);
                })
                .catch((error) => {
                    reject(error)
                })
        } else if (method === 'delete') {
            HTTP.delete(url, param)
                .then(response => {
                    resolve(response);
                }, err => {
                    reject(err);
                })
                .catch((error) => {
                    reject(error)
                })
        } else if (method === 'put') {
            HTTP.put(url, param)
                .then(response => {
                    resolve(response);
                }, err => {
                    reject(err);
                })
                .catch((error) => {
                    reject(error)
                })
        }

    })
}


