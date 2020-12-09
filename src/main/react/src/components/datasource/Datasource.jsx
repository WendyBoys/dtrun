import React from 'react';
import Show from './Show';
import Create from './Create';
import Update from './Update';
import { Switch, Route, Redirect } from 'react-router-dom';
import Header from "../Header";
import Right from "../Right";
export default class Index extends React.Component {

    render() {
        return <div>           
                <Switch>
                    <Route path="/datasource/create" component={Create} />
                    <Route path="/datasource/show" component={Show} />
                    <Route path="/datasource/update/:id" component={Update} />
                </Switch>      
        </div>
    }
}

