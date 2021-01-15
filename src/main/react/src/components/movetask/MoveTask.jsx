import React from 'react';
import Create from './Create';
import Show from './Show';
import Update from './Update';
import {Redirect, Route, Switch} from 'react-router-dom';

export default class Index extends React.Component {

    render() {
        return <div>
            <Switch>
                <Route path="/movetask/show" component={Show}/>
                <Route path="/movetask/create" component={Create}/>
                <Route path="/movetask/update/:id" component={Update}/>
                <Redirect to="/movetask/show"/>
            </Switch>
        </div>
    }
}


