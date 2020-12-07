import React from 'react';
export default class Right extends React.Component {

    render() {
        return <div className="sidebar-group d-print-none">

            <div className="sidebar primary-sidebar show" id="storage">
                <div className="sidebar-header">
                    <h4>Storage Overview</h4>
                </div>
                <div className="sidebar-content">
                    <div id="justgage_five" className="mb-3"></div>
                    <div>
                        <div className="list-group list-group-flush mb-3">
                            <a href="#" className="list-group-item px-0 d-flex align-items-center">
                                <div className="mr-3">
                                    <figure className="avatar">
                                        <span className="avatar-title bg-primary-bright text-primary rounded">
                                            <i className="ti-image"></i>
                                        </span>
                                    </figure>
                                </div>
                                <div className="flex-grow-1">
                                    <p className="mb-0">Images</p>
                                    <span className="small text-muted">259 Files</span>
                                </div>
                                <div>
                                    <h5 className="text-primary">15.7 GB</h5>
                                </div>
                            </a>
                            <a href="#" className="list-group-item px-0 d-flex align-items-center">
                                <div className="mr-3">
                                    <figure className="avatar">
                                        <span className="avatar-title bg-primary-bright text-primary rounded">
                                            <i className="ti-control-play"></i>
                                        </span>
                                    </figure>
                                </div>
                                <div className="flex-grow-1">
                                    <p className="mb-0">Videos</p>
                                    <span className="small text-muted">8 Files</span>
                                </div>
                                <div>
                                    <h5 className="text-primary">20 GB</h5>
                                </div>
                            </a>
                            <a href="#" className="list-group-item px-0 d-flex align-items-center">
                                <div className="mr-3">
                                    <figure className="avatar">
                                        <span className="avatar-title bg-primary-bright text-primary rounded">
                                            <i className="ti-files"></i>
                                        </span>
                                    </figure>
                                </div>
                                <div className="flex-grow-1">
                                    <p className="mb-0">Documents</p>
                                    <span className="small text-muted">46 Files</span>
                                </div>
                                <div>
                                    <h5 className="text-primary">10.5 GB</h5>
                                </div>
                            </a>
                            <a href="#" className="list-group-item px-0 d-flex align-items-center">
                                <div className="mr-3">
                                    <figure className="avatar">
                                        <span className="avatar-title bg-primary-bright text-primary rounded">
                                            <i className="ti-file"></i>
                                        </span>
                                    </figure>
                                </div>
                                <div className="flex-grow-1">
                                    <p className="mb-0">Other Files</p>
                                    <span className="small text-muted">50 Files</span>
                                </div>
                                <div>
                                    <h5 className="text-primary">2.8 GB</h5>
                                </div>
                            </a>
                        </div>
                    </div>
                    <div className="card border shadow-none">
                        <div className="card-body text-center">
                            <img className="img-fluid mb-3" src="https://cdn.jsdelivr.net/gh/WendyBoys/oss/img/icon.png" alt="upgrade" />
                            <h5>Get an Upgrade</h5>
                            <p className="text-muted">Get additional 500 GB space for your documents and files.
                            Unlock now
                            for more space.</p>
                            <button className="btn btn-primary">Upgrade</button>
                        </div>
                    </div>
                </div>
                <div className="sidebar-footer">
                    <button className="btn btn-lg btn-block btn-outline-primary">
                        <i className="fa fa-cloud-upload mr-3"></i> Upload
                </button>
                </div>
            </div>
        </div>



    }
}