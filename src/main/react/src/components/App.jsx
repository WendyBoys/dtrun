import React from 'react';
import { Route, Switch, Redirect } from 'react-router-dom';
import Login from './user/Login';
import Index from './Index';
import axios from 'axios';
import cookie from 'react-cookies'
import Register from './user/Register';
import Datasource from "./datasource/Datasource";
import Dashboard from './Dashboard';
import Create from './datasource/Create';
import Show from './datasource/Show';
import Update from './datasource/Update';
export default class App extends React.Component {


    componentWillMount() {
        const currentUrl = window.location.href;
        axios.interceptors.request.use(function (config) {
            const token = cookie.load('token');
            if (currentUrl.endsWith('login') || currentUrl.endsWith('register')) {

            }
            else {
                if (token === undefined || token.trim() === '') {
                    window.location.href = '/login'
                }
            }
            config.headers.common['token'] = token;
            return config;
        });
    }




    render() {
        return <div>
            <Switch>
                <Route path="/register" component={Register} />
                <Route path="/login" component={Login} />
                <Route exact={true} path="/" component={Login} />
                <Route path="/index" component={Index} />
                <Route path="/dashboard" component={Index} />
                <Route path="/datasource" component={Datasource} />
            </Switch>
        </div>

    }
}