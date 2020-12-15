import React from 'react';
import Show from './Show';
import Create from './Create';
import Update from './Update';
import { Switch, Route, Redirect } from 'react-router-dom';
import  Modify from "../user/Modify"
export default class Index extends React.Component {

    render() {
        return <div>           
                <Switch>
                    <Route path="/datasource/modify" component={Modify}/>
                    <Route path="/datasource/create" component={Create} />
                    <Route path="/datasource/show" component={Show} />
                    <Route path="/datasource/update/:id" component={Update} />
                </Switch>      
        </div>
    }
}


