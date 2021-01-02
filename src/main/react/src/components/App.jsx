import React from 'react';
import {Route, Switch} from 'react-router-dom';
import Login from './user/Login';
import Index from './dashboard/Index';
import axios from 'axios';
import cookie from 'react-cookies'
import Register from './user/Register';


import 'antd/dist/antd.css';
import '../css/css.css';
import {notification} from "antd";

export default class App extends React.Component {


    componentWillMount() {
        //const url='http://127.0.0.1:8080';
        const url = 'https://api.dtrun.cn';
        axios.interceptors.request.use(function (config) {
            const currentUrl = window.location.href;
            const token = cookie.load('token');
            if (currentUrl.endsWith('/') || currentUrl.endsWith('login') || currentUrl.endsWith('register')) {

            } else {
                if (token === undefined || token.trim() === '') {
                    window.location.href = '/login'
                }
            }
            config.headers.common['token'] = token;
            config.headers.common['Access-Control-Max-Age'] = 86400;
            if (config.url.indexOf(url) === -1) {
                config.url = url + config.url;
            }
            return config;
        });

        axios.interceptors.response.use(function (response) {
            // Do something with response data
            return response;
        }, error => {
            if (error) {
                notification['error']({
                    message: '通知',
                    description:
                        '操作失败，请稍后重试！',
                    duration: 2,
                });
            }
            return Promise.reject(error);
        });
    }


    render() {
        return <Switch>
            <Route path="/register" component={Register} />
            <Route path="/login" component={Login} />
            <Route exact={true} path="/" component={Login} />
            <Route path="/index" component={Index} />
            <Route path="/dashboard" component={Index} />
            <Route path="/datasource" component={Index} />
            <Route path="/movetask" component={Index} />
        </Switch>

    }
}

