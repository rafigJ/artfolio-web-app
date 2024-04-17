import {useState} from "react";

type FetchingCallback = (...args: any[]) => Promise<void>;

export const useFetching = (callback: FetchingCallback): [FetchingCallback, boolean, boolean, string] => {
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');
    const [isError, setIsError] = useState(false);

    const fetching: FetchingCallback = async (...args) => {
        try {
            setIsLoading(true);
            await callback(...args);
        } catch (e: any) {
            setIsError(true)
            setError(e.message);
        } finally {
            setIsLoading(false);
        }
    };

    return [fetching, isLoading, isError, error];
};
