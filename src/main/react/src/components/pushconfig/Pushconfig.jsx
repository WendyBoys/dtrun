import React from 'react';
import Contact from './Contact';
import Create from './Create';
import Update from './Update';
import {Route, Switch,Redirect} from 'react-router-dom';

export default class Pushconfig extends React.Component {

    render() {
        return <Switch>
            <Route path="/pushconfig/contact" component={Contact}/>
            <Route path="/pushconfig/create" component={Create}/>
            <Route path="/pushconfig/update/:id" component={Update}/>
            <Redirect to="/pushconfig/contact"/>
        </Switch>

    }
}


