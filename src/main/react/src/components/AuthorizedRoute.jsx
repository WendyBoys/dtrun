import {Component, React} from 'react';
import {Redirect, Route} from 'react-router-dom';
import cookie from 'react-cookies'


export default class AuthorizedRoute extends Component {

    render() {
        return (
            <div style={{height: '100%'}}>
                {
                    cookie.load('token') ? <Route {...this.props}/> : <Redirect to="/login"/>
                }
            </div>
        )
    }

}
