import {React} from 'react';
import { BranchCodeDetails } from './BranchCodeDetails';
import styles from '../styles/SwiftCodeDetails.module.css';

export function HeadquarterCodeDetails({code}) {
    return (
        <article className={styles.panel}>
            <h1>Headquarters SWIFT code details</h1>
            <p>Bank name: {code.bankName}</p>
            <p>Address: {code.address}</p>
            <p>Country ISO2 code: {code.countryISO2}</p>
            <p>Country name: {code.countryName}</p>
            <p>SWIFT code: {code.swiftCode}</p>
            { code.branches.length !== 0 && <h2>Branches</h2 > }
            <ul>
                { code.branches.map(branch => <li><BranchCodeDetails key={branch.swiftCode} code={branch}/></li>) }
            </ul>
            
        </article>
    );
}