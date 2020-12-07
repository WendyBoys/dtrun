import React, { Component } from 'react';
export default class Register extends React.Component {

    render() {
        return <div id="registerSpace" className="modal-dialog" style={{ marginTop: '10%' }}>
            <div className="modal-content">
                <div className="modal-header">

                    <h4 className="modal-title text-center" id="registerLabel">注册</h4>
                </div>
                <div className="modal-body" id="model-body1">

                    <div className="form-group">

                        <input type="text" id="registerAccount" className="form-control" placeholder="账号" autoComplete="off" />
                    </div>
                    <div className="form-group">

                        <input type="password" id="registerPassword" className="form-control" placeholder="密码" autoComplete="off" />
                    </div>
                </div>

                <div className="modal-footer">

                    <div id="goLogin" className="pull-left" style={{ cursor: 'pointer' }}  >已有账号?直接登录</div>

                    <button type="button" id="register" style={{ width: '100%', outline: 'none' }} className="btn btn-primary form-control">注册
                    </button>


                </div>

            </div>
        </div>

    }
}