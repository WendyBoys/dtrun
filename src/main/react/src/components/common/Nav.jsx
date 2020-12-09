import React from 'react';
import { NavLink } from 'react-router-dom';
import logo from '../../images/logo.png';

export default class Nav extends React.Component {

    render() {
        return <div className="navigation">
            <div className="logo">
                <NavLink to="/dashboard">
                    <img src={logo} />
                </NavLink>
            </div>
            <ul>
                <li>
                    <NavLink to="/dashboard">
                        <i className="nav-link-icon ti-pie-chart"></i>
                        <span className="nav-link-label">运维大盘</span>
                        <span className="badge badge-danger badge-small">2</span>
                    </NavLink>

                </li>
                <li>
                    <NavLink to="/datasource/show">
                        <i className="nav-link-icon ti-layers"></i>
                        <span className="nav-link-label">数据源管理</span>
                    </NavLink>

                </li>
                <li>
                    <NavLink to="/movetask/show">
                        <i className="nav-link-icon ti-file"></i>
                        <span className="nav-link-label">迁移任务管理</span>
                    </NavLink>

                </li>
                <li>
                    <NavLink to="/syswarning">
                        <i className="nav-link-icon ti-pulse"></i>
                        <span className="nav-link-label">系统预警</span>
                        <span className="badge badge-warning">New</span>
                    </NavLink>

                </li>
                <li className="flex-grow-1">
                    <NavLink to="/users">
                        <i className="nav-link-icon ti-user"></i>
                        <span className="nav-link-label">用户管理</span>
                    </NavLink>

                </li>
                <li>
                    <NavLink to="/settings">
                        <i className="nav-link-icon ti-settings"></i>
                        <span className="nav-link-label">系统设置</span>
                    </NavLink>

                </li>
            </ul>
        </div>


    }
}