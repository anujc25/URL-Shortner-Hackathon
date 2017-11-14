import React, { Component } from 'react';
import './App.css';
import Shortner from './components/Shortner';

class App extends Component {
  // Initialize state
  state = { passwords: [] }

  // Fetch passwords after first mount
  componentDidMount() {
    this.getPasswords();
  }

  getPasswords = () => {
    // Get the passwords and store them in state
    fetch('/api/passwords')
      .then(res => res.json())
      .then(passwords => this.setState({ passwords }));
  }

  render() {
    const { passwords } = this.state;

    return (
      <div className="App container-fluid">
        <div className="row App-header">
          <div className="col col-md-4 offset-md-2">URL Shortner</div>
        </div>
          <Shortner/>
      </div>
    );
  }
}

export default App;
