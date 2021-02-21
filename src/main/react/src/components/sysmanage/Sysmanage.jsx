import React from 'react';
import Logs from './Logs';
import {Route, Switch} from 'react-router-dom';

export default class Sysmanage extends React.Component {

    render() {
        return <Switch>
            <Route path="/sysmanage/logs" component={Logs}/>
        </Switch>

    }
}


