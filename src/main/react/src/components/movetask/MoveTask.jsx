import React from 'react';
import Create from './Create';
import {Redirect, Route, Switch} from 'react-router-dom';

export default class Index extends React.Component {

    render() {
        return <div>                 
                <Switch>
                <Route path="/movetask/create" component={Create} />
                <Redirect to="/movetask/create"/>          
                </Switch>      
        </div>
    }
}


