package com.sigopt.net;

import com.sigopt.Sigopt;
import com.sigopt.model.APIResource;
import com.sigopt.model.Experiment;
import com.sigopt.model.MockResource;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
    APIResource.class
})
public class APIMethodCallerTest {
    APIMethodCaller<MockResource> caller;

    @BeforeClass
    public static void setUp() {
        Sigopt.clientToken = "client-token";
    }

    @Before
    public void setUpMockData() {
        caller = new APIMethodCaller("get", "/path", MockResource.class);
    }

    @Test
    public void callWithAPIResource() throws Exception {
        APIMethod.Builder builder = Mockito.mock(APIMethod.Builder.class);
        APIMethod method = Mockito.mock(APIMethod.class);
        caller.apiMethodBuilder = builder;
        Mockito.when(builder.build()).thenReturn(method);
        Mockito.stub(method.execute()).toReturn(null);
        method.response = new Requester.Response("{}", 200);

        MockResource expected = new MockResource("exp-id", "exp-name");
        PowerMockito.mockStatic(APIResource.class);
        PowerMockito.when(APIResource.constructFromJson(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(expected);

        assertEquals(expected, caller.call());
    }
}
