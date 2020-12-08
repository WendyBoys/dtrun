import React from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';
import Dashboard from './Dashboard';
import Header from './Header';
import Right from './Right';
import Nav from './Nav';






export default class Index extends React.Component {



    render() {
        return <div>
            <div className="layout-wrapper">
                <Header />
                <Right />
                <Nav />
                <Dashboard />
            </div>
        </div>
    }
}