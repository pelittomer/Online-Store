import { apiSlice } from "../store/apiSlice";

export const uploadApiSlice = apiSlice.injectEndpoints({
    endpoints: builder => ({
        getUploadFile: builder.query<string, string>({
            query: (fileId) => ({
                url: `/upload/${fileId}`,
                method: 'GET',
                responseHandler: async (response) => {
                    if (!response.ok) {
                        const error = await response.text();
                        throw new Error(error);
                    }
                    const arrayBuffer = await response.arrayBuffer();
                    const base64 = btoa(
                        new Uint8Array(arrayBuffer).reduce(
                            (data, byte) => data + String.fromCharCode(byte),
                            ''
                        )
                    );
                    return `data:image/png;base64,${base64}`;
                },
            }),
        }),
    })
});

export const {
    useGetUploadFileQuery
} = uploadApiSlice;