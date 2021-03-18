import React from 'react';
import Contact from './Contact';
import {Route, Switch} from 'react-router-dom';

export default class Pushconfig extends React.Component {

    render() {
        return <Switch>
            <Route path="/pushconfig/contact" component={Contact}/>
        </Switch>

    }
}


