import React from 'react';
import Logs from './Logs';
import Whitelist from './Whitelist';
import Create from './Create';
import Update from './Update';
import {Route, Switch} from 'react-router-dom';

export default class Sysmanage extends React.Component {

    render() {
        return <Switch>
            <Route path="/sysmanage/logs" component={Logs}/>
            <Route path="/sysmanage/whitelist" component={Whitelist}/>
            <Route path="/sysmanage/create" component={Create}/>
            <Route path="/sysmanage/update/:id" component={Update}/>
        </Switch>

    }
}


