import React from 'react';
import {Route, Switch} from 'react-router-dom';
import Login from './user/Login';
import Index from './dashboard/Index';
import Register from './user/Register';
import AuthorizedRoute from './AuthorizedRoute'

import 'antd/dist/antd.css';
import '../css/css.css';

export default class App extends React.Component {

    render() {
        return <Switch>
            <Route path="/register" component={Register}/>
            <Route path="/login" component={Login}/>
            <AuthorizedRoute exact={true} path="/" component={Index}/>
            <AuthorizedRoute path="/index" component={Index}/>
            <AuthorizedRoute path="/dashboard" component={Index}/>
            <AuthorizedRoute path="/datasource" component={Index}/>
            <AuthorizedRoute path="/movetask" component={Index}/>
            <AuthorizedRoute path="/sysmanage" component={Index}/>
            <AuthorizedRoute path="/about" component={Index}/>
        </Switch>

    }
}

