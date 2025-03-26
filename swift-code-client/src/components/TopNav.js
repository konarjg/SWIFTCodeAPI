import React from 'react';
import {Link} from 'react-router-dom';
import styles from '../styles/TopNav.module.css';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars } from "@fortawesome/free-solid-svg-icons"

export function TopNav() {
    return (
        <nav className={styles.navigationBar}>
            <FontAwesomeIcon icon={faBars} className={styles.menu}/>
            <Link to="/" className={styles.logo}>Remitly SWIFT Code Database</Link>
        </nav>
    );
}