import {React, forwardRef} from 'react';
import {Link} from 'react-router-dom';
import styles from '../styles/Sidebar.module.css';

export const Sidebar = forwardRef(({}, ref) => {
    return (
        <aside className={styles.sidebar} ref={ref}>
            <div>
                <Link to="/" className={styles.link}>Home</Link>
                <Link to="/swift-code-details" className={styles.link}>Retrieve details for a specific SWIFT code</Link>
                <Link to="/swift-codes-by-country" className={styles.link}>Retrieve all SWIFT codes for a specific country ISO2 code</Link>
                <Link to="/add-swift-code" className={styles.link}>Add a new SWIFT code to the database</Link>
                <Link to="/delete-swift-code" className={styles.link}>Remove an existing SWIFT code from the database</Link>
            </div>
            <div className={styles.filler} />
        </aside>
    );
});