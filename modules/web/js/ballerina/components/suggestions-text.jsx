/**
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import React from 'react';
import ReactDOM from 'react-dom';
import PropTypes from 'prop-types';
import Autosuggest from 'react-autosuggest';
import './suggestions-text.css'

class SuggestionsText extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: '',
            suggestions: []
        };

        this.onSuggestionsFetchRequested = ({value}) => {
            this.setState({suggestions: this.getSuggestions(value)});
        }

        this.renderSuggestionsContainer = this.renderSuggestionsContainer.bind(this);
        this.onChange = this.onChange.bind(this);
        this.onKeyDown = this.onKeyDown.bind(this);
    }

    renderSuggestion(suggestion) {
        return (
            <div>
              {suggestion.name}
            </div>
        );
    }

    getSuggestionValue(suggestion) {
        return suggestion.name;
    }

    getSuggestions(query) {
        var matches = [];

        var substrRegex = new RegExp(query, 'i');

        this.props.suggestionsPool.forEach(sug => {
            if (substrRegex.test(sug.name)) {
                matches.push(sug);
            }
        });

        return matches;
    }

    onKeyDown(e) {
        if(e.keyCode === 13) {
            this.props.onEnter(this.state.value);
            this.setState({
                value: ''
            });
        }
    }

    onChange(event, { newValue, method }) {
        this.setState({value: newValue});
    }

    componentWillUnmount() {
        ReactDOM.render(<noscript/>, this.context.overlay);
    }

    renderSuggestionsContainer({ containerProps , children, query }) {
        const {x, y, onClick, height=25, width=100} = this.props;
        const allProps = {
          style: {
              position: 'absolute',
              top: y + height,
              left: x,
              width
          }
        }

        Object.assign(allProps, containerProps);

        return (
            <div {...allProps}>
                {children}
            </div>
        );
    }

    renderSuggestionsText() {
        if(!this.props.show) {
            ReactDOM.render(<noscript/>, this.context.overlay);
            return;
        }

        const {x, y, onClick, height=25, width=100} = this.props;
        const textProps = {x, y, onClick};

        const inputStyle = {
            position: 'absolute',
            top: y,
            left: x,
            width,
            height
        };

        const inputProps = {
          value: this.state.value,
          onChange: this.onChange,
          onKeyDown: this.onKeyDown,
          style: inputStyle
        };

        ReactDOM.render(
          <Autosuggest
            suggestions={this.state.suggestions}
            onSuggestionsFetchRequested={this.onSuggestionsFetchRequested}
            getSuggestionValue={this.getSuggestionValue}
            renderSuggestion={this.renderSuggestion}
            renderSuggestionsContainer={this.renderSuggestionsContainer}
            inputProps={inputProps}
          />, this.context.overlay
        );
    }

    componentDidUpdate(prevProps) {
        this.renderSuggestionsText()
    }

    componentDidMount() {
        this.renderSuggestionsText();
    }

    render() {
        const {x, y, height, width} = this.props;

        const style = {
            fill: "#eee"
        }

        if(!this.props.show) {
            style.display = 'none'
        }

        return <rect x={x} y={y} height={height} width={width} style={style} />
    }
}

SuggestionsText.contextTypes = {
    overlay: PropTypes.instanceOf(Object).isRequired,
};

export default SuggestionsText;
