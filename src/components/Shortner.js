import React, {Component} from 'react';

class Shortner extends Component{
    state = {
    }

    onShortUrlClicked = (event) => {
        console.log(event);
    }
        
    render(){
        return(
            <div className="Section-ShortenURL">
            
                <div className="row">
                    <div className="offset-md-2 shorten-title">Simplify your links</div>
                </div>
    
                <div className="row">
                    <div className="col-md-8 offset-md-2 shorten-input input-group input-group-lg">              
                        <input type="text" className="form-control input-lg" placeholder="Your original URL here"></input>
                        
                            <button className="btn btn-default shorten-btn" type="button" onClick={(event) => this.onShortUrlClicked(event)}>SHORTEN URL</button>
                        
                    </div> 
                </div>
            </div>
        )
    }
};

export default Shortner;