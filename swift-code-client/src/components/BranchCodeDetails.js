import {React} from 'react';
import styles from '../styles/SwiftCodeDetails.module.css';

export function BranchCodeDetails({code}) {
    return (
        <article className={styles.panel}>
            <h1>Branch SWIFT code details</h1>
            <p>Bank name: {code.bankName}</p>
            <p>Address: {code.address}</p>
            <p>Country ISO2 code: {code.countryISO2}</p>
            { code.hasOwnProperty("countryName") && <p>Country name: {code.countryName}</p> }
            <p>SWIFT code: {code.swiftCode}</p>
        </article>
    );
}