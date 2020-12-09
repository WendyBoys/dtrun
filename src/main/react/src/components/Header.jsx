import React from 'react';
import cookie from 'react-cookies'
import {Menu, Dropdown, Button, Space, notification} from 'antd';
import axios from "axios";

export default class Header extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            iconUrl: '',
        }
    }

    componentDidMount() {
        axios.get('http://localhost/user/getCurrentUser').then((response) => {
            var result = response.data.message;
            if (result == 'Success') {
                const user = response.data.data;
                this.setState({username: user.username, iconUrl: user.iconUrl})
            } else {

            }
        });
    }


    logout() {
        cookie.remove('token');
        this.props.history.push('/login')
        notification['success']({
            message: '通知',
            description:
                '注销成功',
            duration: 2,
        });
    };

    menu = (
        <Menu>
            <Menu.Item>
                <a target="_blank" rel="noopener noreferrer" href="">
                    个人中心
                </a>
            </Menu.Item>
            <Menu.Item>
                <a target="_blank" rel="noopener noreferrer" href="">
                    修改密码
                </a>
            </Menu.Item>
            <Menu.Item>
                <a target="_blank" rel="noopener noreferrer" onClick={() => this.logout()}>
                    注销
                </a>
            </Menu.Item>
        </Menu>
    );


    render() {
        return <div className="header d-print-none">
            <div className="header-container">
                <div className="header-body">
                    <div className="header-body-left">
                        <ul className="navbar-nav">
                            <li className="nav-item navigation-toggler">
                                <a href="#" className="nav-link">
                                    <i className="ti-menu"></i>
                                </a>
                            </li>
                            <li className="nav-item">
                                <div className="header-search-form">
                                    <form>
                                        <div className="input-group">
                                            <div className="input-group-prepend">
                                                <button className="btn">
                                                    <i className="ti-search"></i>
                                                </button>
                                            </div>
                                            <input type="text" id="search" className="form-control"
                                                   placeholder="Search something..."/>
                                            <div className="input-group-append">
                                                <button className="btn header-search-close-btn">
                                                    <i data-feather="x"></i>
                                                </button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </li>
                        </ul>
                    </div>

                    <div className="header-body-right">
                        <ul className="navbar-nav">
                            <li className="nav-item">
                                <a href="#" className="nav-link mobile-header-search-btn" title="Search">
                                    <i className="ti-search"></i>
                                </a>
                            </li>
                            <li className="nav-item">
                                <a href="#" className="nav-link" title="Dark">
                                    <i className="fa fa-moon-o"></i>
                                </a>
                            </li>

                            <li className="nav-item dropdown">
                                <a href="#" className="nav-link nav-link-notify" title="Notifications"
                                   data-toggle="dropdown">
                                    <i className="ti-bell"></i>
                                </a>
                                <div className="dropdown-menu dropdown-menu-right dropdown-menu-big">
                                    <div className="bg-primary px-3 py-3">
                                        <h6 className="mb-0">Notifications</h6>
                                    </div>
                                    <div className="dropdown-scroll">
                                        <ul className="list-group list-group-flush">
                                            <li>
                                                <a href="#" className="list-group-item d-flex hide-show-toggler">
                                                    <div>
                                                        <figure className="avatar mr-3">
                                                            <span
                                                                className="avatar-title bg-secondary-bright text-secondary rounded-circle">
                                                                <i className="ti-server"></i>
                                                            </span>
                                                        </figure>
                                                    </div>
                                                    <div className="flex-grow-1">
                                                        <p className="mb-0">
                                                            Your storage space is running low!
                                                            <i title="Mark as unread" data-toggle="tooltip"
                                                               className="hide-show-toggler-item fa fa-check font-size-11 position-absolute right-0 top-0 mr-3 mt-3"></i>
                                                        </p>
                                                        <span className="text-muted small">4 sec ago</span>
                                                    </div>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="#" className="list-group-item d-flex hide-show-toggler">
                                                    <div>
                                                        <figure className="avatar mr-3">
                                                            <img
                                                                src="https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/icon.png"
                                                                className="rounded-circle" alt="avatar"/>
                                                        </figure>
                                                    </div>
                                                    <div>
                                                        <p className="mb-0">
                                                            <span className="text-primary">Jonny Richie</span> uploaded
                                                            new
                                                            files
                                                            <i title="Mark as read" data-toggle="tooltip"
                                                               className="hide-show-toggler-item fa fa-circle-o font-size-11 position-absolute right-0 top-0 mr-3 mt-3"></i>
                                                        </p>
                                                        <span className="text-muted small">20 min ago</span>
                                                    </div>
                                                </a>
                                            </li>
                                            <li className="text-divider text-center small pb-2 px-3">
                                                <span>Old notifications</span>
                                            </li>
                                            <li>
                                                <a href="#" className="list-group-item d-flex hide-show-toggler">
                                                    <div>
                                                        <figure className="avatar mr-3">
                                                            <span
                                                                className="avatar-title bg-info-bright text-info rounded-circle">
                                                                <i className="fa fa-cloud-upload"></i>
                                                            </span>
                                                        </figure>
                                                    </div>
                                                    <div className="flex-grow-1">
                                                        <p className="mb-0">
                                                            Upgrade plan
                                                            <i title="Mark as unread" data-toggle="tooltip"
                                                               className="hide-show-toggler-item fa fa-check font-size-11 position-absolute right-0 top-0 mr-3 mt-3"></i>
                                                        </p>
                                                        <span className="text-muted small">45 sec ago</span>
                                                    </div>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="#" className="list-group-item d-flex hide-show-toggler">
                                                    <div>
                                                        <figure className="avatar mr-3">
                                                            <span
                                                                className="avatar-title bg-success-bright text-success rounded-circle">
                                                                <i className="ti-share"></i>
                                                            </span>
                                                        </figure>
                                                    </div>
                                                    <div className="flex-grow-1">
                                                        <p className="mb-0">
                                                            A file has been shared
                                                            <i title="Mark as unread" data-toggle="tooltip"
                                                               className="hide-show-toggler-item fa fa-check font-size-11 position-absolute right-0 top-0 mr-3 mt-3"></i>
                                                        </p>
                                                        <span className="text-muted small">58 sec ago</span>
                                                    </div>
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                    <div className="px-3 py-2 text-right border-top">
                                        <ul className="list-inline small">
                                            <li className="list-inline-item mb-0">
                                                <a href="#">Mark All Read</a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </li>

                            <li className="nav-item dropdown">
                                <a href="#" className="nav-link" title="Settings" data-sidebar-target="#settings">
                                    <i className="ti-settings"></i>
                                </a>
                            </li>

                            <li className="nav-item dropdown">
                                <Dropdown overlay={this.menu} placement="topRight">
                                    <a href="#" className="nav-link profile-nav-link dropdown-toggle" title="User menu">
                                        <span className="mr-2 d-sm-inline d-none username">{this.state.username}</span>
                                        <figure className="avatar avatar-sm">
                                            <img src={this.state.iconUrl}
                                                 className="rounded-circle" alt="avatar"/>
                                        </figure>
                                    </a>
                                </Dropdown>
                            </li>
                        </ul>
                    </div>
                </div>

                <ul className="navbar-nav ml-auto">
                    <li className="nav-item header-toggler">
                        <a href="#" className="nav-link">
                            <i className="ti-arrow-down"></i>
                        </a>
                    </li>
                    <li className="nav-item sidebar-toggler">
                        <a href="#" className="nav-link">
                            <i className="ti-cloud"></i>
                        </a>
                    </li>
                </ul>
            </div>
        </div>


    }
}