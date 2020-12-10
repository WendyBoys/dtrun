import React from 'react';
import socket from '../../images/socket.svg';
export default class Dashboard extends React.Component {


    render() {
        return <div className="content-wrapper">
            <div className="content-body">
                <div className="content">
                    <div className="card bg-primary-bright border-0">
                        <div className="card-body">
                            <div className="row">
                                <div className="col-xl-2 col-md-3">
                                    <figure>
                                        <img className="img-fluid" src={socket} alt="upgrade" />
                                    </figure>
                                </div>
                                <div className="col-xl-10 col-md-9">
                                    <div className="d-md-flex align-items-center justify-content-between text-center text-md-left p-4 p-md-0">
                                        <div className="mr-3">
                                            <h4 className="mb-3">Get an Upgrade</h4>
                                            <p className="text-muted">Get additional 500 GB space for your documents and files.
                                            Expand
                                    your storage and enjoy your business. Change plan for more space.</p>
                                            <a href="#" className="small">Close</a>
                                        </div>
                                        <div className="flex-shrink-0 ml-3">
                                            <button className="btn btn-outline-primary btn-lg2">Upgrade Now</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    }
}