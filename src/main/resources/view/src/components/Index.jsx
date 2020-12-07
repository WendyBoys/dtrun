import React from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';
import Dashboard from './Dashboard';
import Header from './Header';
import Right from './Right';
import Nav from './Nav';


import 'antd/dist/antd.css';
import 'bootstrap/dist/css/bootstrap.css';
import '../css/css2.css';
import '../css/bundle.css';
import '../css/prism.css';
import '../css/app.css';




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