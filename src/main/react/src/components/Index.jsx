import React from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';
import Dashboard from './Dashboard';
import Datasource from './datasource/Datasource';
import Header from './Header';
import Right from './Right';
import Nav from './Nav';

export default class Index extends React.Component {
    render() {
        return <div>
            <div className="layout-wrapper">
                <Header {...this.props} />
                <Right />
                <Nav />
                <Switch>
                    <Route path="/dashboard" component={Dashboard} />
                    <Route path="/datasource" component={Datasource} />
                    <Redirect to="/dashboard"/>                
                </Switch>
               
            </div>
        </div>
    }
}